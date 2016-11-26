# clj-influxdb-metrics-reporter

Report metrics from https://github.com/sjl/metrics-clojure to
InfluxDB.

## Installation

lein install

## Usage

(def reporting-future (->
    metrics-registry
    reporter
    (transport {:protocol "http" :host "localhost" :port 8086 :user "" :password "" :db "db" :period 60000})
    start))
...
(future-cancel reporting-future)

## License

Copyright © 2016 Busta Pipes

Distributed under the GNU Lesser General Public License.
