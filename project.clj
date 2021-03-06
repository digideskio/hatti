(defn js-dir
      "Prefix with full JavaScript directory."
      [path]
      (str "resources/public/js/lib/" path))

(defproject onaio/hatti "0.1.24-SNAPSHOT"
  :description "A cljs dataview from your friends at Ona.io"
  :license "Apache 2, see LICENSE"
  :url "https://github.com/onaio/hatti"
  :dependencies [;; Core libraries
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [org.clojure/core.async "0.2.374"]
                 [sablono "0.3.1"]
                 [org.omcljs/om "0.9.0"]
                 [inflections "0.9.7"]
                 [secretary "1.2.3"]
                 [onaio/chimera "0.0.2-SNAPSHOT"]
                 ;; For charts
                 [com.keminglabs/c2 "0.2.4-SNAPSHOT"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [clj-time "0.7.0"]
                 [com.andrewmcveigh/cljs-time "0.2.3"]
                 ;; JS
                 [cljsjs/moment "2.9.0-1"]
                 [onaio/leaflet-cljs "0.7.3-SNAPSHOT"]
                 [cljsjs/oboe "2.1.2-1"]
                 [cljsjs/jquery "1.9.1-0"]
                 [onaio/slickgrid-cljs "0.0.3"]
                 [prabhasp/osmtogeojson-cljs "2.2.5-1"]
                 [org.webjars/SlickGrid "2.1"]
                 ;; For client
                 [cljs-http "0.1.40"]
                 ;; For testing
                 [prismatic/dommy "1.1.0"]]
  :cljfmt {:file-pattern #"[^\.#]*\.clj[s]?$"}
  :plugins [[lein-bikeshed-ona "0.2.1"]
            [lein-cljfmt "0.3.0"]
            [lein-cljsbuild "1.1.3"]
            [lein-kibit "0.1.2"]]
  :clean-targets ["out/hatti" "out/hatti.js"]
  :cljsbuild {:builds
              {:dev {:source-paths ["src"]
                     :compiler {:output-to ~(js-dir "main.js")
                                :output-dir ~(js-dir "out")
                                :optimizations :whitespace
                                :cache-analysis true
                                :pretty-print true
                                :source-map ~(js-dir "main.js.map")}}
               :prod {:source-paths ["src"]
                      :compiler {:output-to ~(js-dir "hatti.js")
                                 :output-dir ~(js-dir "out-prod")
                                 :optimizations :advanced
                                 :pretty-print false}
                      :jar true}
               :test {:source-paths ["src" "test"]
                      :notify-command ["phantomjs"
                                       "phantom/unit-test.js"
                                       "phantom/unit-test.html"
                                       "target/main-test.js"]
                      :compiler {:optimizations :whitespace
                                 :output-to "target/main-test.js"
                                 :pretty-print true}}}
              :test-commands {"unit-test"
                              ["phantomjs"
                               "phantom/unit-test.js"
                               "phantom/unit-test.html"
                               "target/main-test.js"]}})
