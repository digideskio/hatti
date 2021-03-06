(ns hatti.test-utils
  (:require [dommy.core :as dommy])
  (:require-macros [dommy.core :refer [sel sel1]]))

;;====================
;; PERMISSION DEFINITIONS
;;====================
(def owner "owner")
(def readonly "readonly")

;;=====================
;; UTIL FUNCTIONS
;;=====================
(defn format
  "Formats a string using goog.string.format, so we can use format in cljx."
  [fmt & args]
  (apply goog.string/format fmt args))

(defn texts [els]
  (map dommy/text els))

(defn attrs [attr els]
  (map #(dommy/attr attr %) els))

(defn remove-nil
  "Remove nil values from a sequence."
  [l]
  (remove nil? l))

(defn ordered-diff
  "Return difference between 2 sequences. Preserves ordering in first seq."
  [a b]
  (filter #(not (contains? (set b) %)) a))

;;======================
;; SAMPLE DATA AND FORMS
;;======================

(defn form-gen [n]
  (for [i (range n)]
    {:type (rand-nth ["string" "select one"])
     :full-name (str "hello" i)
     :name (str "hello" i)
     :label (str "Hello: " i)}))

(defn data-gen [ncol nrow]
  (let [rf (fn [max] (format "%02d" (inc (rand-int max))))]
    (for [i (range nrow)]
      (apply merge {"_id" i
                    "_rank" (inc i)
                    "_submission_time" (str "2012-" (rf 12) "-" (rf 30))}
             (for [j (range ncol)] {(str "hello" j) (str "goodbye" j)})))))

(def thin-form (form-gen 1))
(def fat-form (form-gen 100))

(def no-data (data-gen 0 0))
(def small-thin-data (data-gen 1 10))
(def big-thin-data   (data-gen 1 100))
(def small-fat-data  (data-gen 100 10))
(def big-fat-data    (data-gen 100 100))

;;======================
;; CONTAINER GEN HELPER
;;======================

(defn new-container!
  "Returns a new div in the DOM, mostly used for rendering om components into.
   id is passed in or randomly generated, parent-selector is passed in or :body.
   Returns: HTMLDivElement."
  ([] (new-container! (str "container-" (gensym))))
  ([id] (new-container! id :body))
  ([id parent-selector]
   (let [div (.createElement js/document "div")
         meta (.createElement js/document "meta")]
     (.setAttribute div "id" id)
     (.setAttribute meta "name" "language-code")
     (.setAttribute meta "content" "en")
     (dommy/append! (sel1 js/document parent-selector) meta div)
     div)))
