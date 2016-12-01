; Copyright 2016 Busta Pipes
; This file is part of clj-influxdb-metrics-reporter
;
; clj-influxdb-metrics-reporter is free software: you can redistribute it and/or modify
; it under the terms of the GNU General Public License as published by
; the Free Software Foundation, either version 3 of the License, or
; (at your option) any later version.
;
; clj-influxdb-metrics-reporter is distributed in the hope that it will be useful,
; but WITHOUT ANY WARRANTY; without even the implied warranty of
; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
; GNU General Public License for more details.
;
; You should have received a copy of the GNU General Public License
; along with clj-influxdb-metrics-reporter  If not, see <http://www.gnu.org/licenses/>.
(ns clj-influxdb-metrics-reporter.core
  (:require [clojure.string :as s]
            [clojure.tools.logging :as log]
            [org.httpkit.client :as http])
  (:import [java.net URL]))

(defn reporter "Starting point for creating a reporter" [registry]
  (hash-map :registry registry))

(defn transport
  "Protocol, host, port, user, password, database, period"
  [config influx-db-config]
  (assoc config :influx-db influx-db-config))

(defn- url [influx-db]
  (URL. (:protocol influx-db)
        (:host influx-db)
        (:port influx-db)
        (str "/write?db=" (:db influx-db))))

(defn- specify-default [coll lookup default]
  (if (contains? coll lookup)
    (lookup coll)
    default))

(defn- influx-tagger [metric-name]
  (let [all (s/split metric-name #"\.")
        tags (pop all)
        measurement (last all)]
    (->>
      (if (even? (count tags)) tags (pop tags))
      (apply array-map)
      (map #(str (first %) "=" (second %)))
      (cons measurement)
      (s/join ","))))

(defmulti influx-value (fn [metric] (keyword (.getName (class metric)))))

(defmethod influx-value :com.codahale.metrics.Counter [metric]
  (format "count=%di" (.getCount metric)))

(defmethod influx-value :com.codahale.metrics.Gauge [metric]
  (format "value=%f" (.getValue metric)))

(defmethod influx-value :com.codahale.metrics.Histogram [metric]
  (format "count=%di" (.getCount metric)))

(defmethod influx-value :com.codahale.metrics.Meter [metric]
  (format "count=%di,fifteen-minute=%f,five-minute=%f,one-minute=%f,mean=%f"
          (.getCount metric)
          (.getFifteenMinuteRate metric)
          (.getFiveMinuteRate metric)
          (.getOneMinuteRate metric)
          (.getMeanRate metric)))

(defn- connect [config]
  (let [influx-db (:influx-db config)]
    (hash-map
      :config config
      :period (:period influx-db)
      :user (:user influx-db)
      :password (:password influx-db)
      :timeout (specify-default influx-db :timeout 2000)
      :tagger-fn (specify-default config :metric-tagger-fn influx-tagger)
      :value-fn (specify-default config :metric-value-fn influx-value)
      :url (url influx-db))))

(defn- format-metrics [registry tagger-fn value-fn]
  (try
    (str
      (->>
        registry
        (.getMetrics)
        (into {})
        (map #(vector (tagger-fn (first %)) (value-fn (second %))))
        (map (partial s/join " "))
        (s/join "\n"))
      "\n")
    (catch Exception e (log/error "Failure during format preparation: " e))))

(defn- send-metrics [url options]
  (try
    (let [result @(http/post url options)]
      (if-not (= 204 (:status result))
        (let [body (:body result)
              message (if body (.trim body) (str "Empty result: " result))]
          (log/warn (str "Bad status from InfluxDB: " message)))))
    (catch Exception e
      (hash-map :error {:exception e :message (.getMessage e)}))))

(defn- loop-core [connection]
  (try
    (Thread/sleep (:period connection))
    (let [metrics (format-metrics (:registry (:config connection))
                                  (:tagger-fn connection)
                                  (:value-fn connection))
          result (send-metrics
                   (str (:url connection))
                   {:timeout (:timeout connection)
                    :basic-auth [(:user connection) (:password connection)]
                    :body metrics})]
      (if (:error result)
        (log/warn "Failure sending to InfluxDB: " (:exception (:error result))))
      true)
    (catch InterruptedException e (log/info "Terminating report loop."))))

(defn- report-loop [connection]
  (loop []
    (let [result (loop-core connection)]
      (if result
        (recur)))))

(defn start "Start the reporter thread" [config]
  (let [connection (connect config)]
    (future (report-loop connection))))
