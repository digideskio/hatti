(ns hatti.views.dataview-test
  (:require-macros [cljs.test :refer (is deftest testing)])
  (:require [cljs.test :as t]
            [hatti.shared :as shared]
            [hatti.test-utils :refer [format]]))

(defn data-gen [ncol nrow]
  (let [rf (fn [max] (format "%02d" (inc (rand-int max))))]
    (for [i (range nrow)]
      (apply merge {"_id" i
                    "_rank" (inc i)
                    "_submission_time" (str "2012-" (rf 12) "-" (rf 28))}
             (for [j (range ncol)] {(str "hello" j) (str "goodbye" j)})))))

(def big-thin-data (data-gen 1 100))

;; INITIAL STATE TESTS
(deftest initial-state
  (let [_ (shared/update-app-data! shared/app-state big-thin-data :rerank? true)
        data-100 (get-in @shared/app-state [:data])
        dget (fn [k d] (map #(get % k) d))]
    (testing "data is initialized properly"
      (is (= (-> data-100 first count) (-> big-thin-data first count)))
      (is (= 100 (count data-100))))
    (testing "data has correct _rank attributes"
      (is (= (dget "_rank" data-100) (map inc (range 100)))))
    (testing "data is in sorted order by _submission_time"
      (let [test-dates (map #(.format (js/moment %))
                            ["2012-01-01" "2013-01-01" "2011-01-01"])
            s100 (map #(.format (js/moment %))
                      (dget "_submission_time" data-100))]
        (is (= (sort test-dates) (conj (take 2 test-dates) (last test-dates))))
        (is (= s100 (sort s100)))))))
