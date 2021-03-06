(ns hatti.utils.style-test
  (:require-macros [cljs.test :refer [is deftest testing]])
  (:require [clojure.string :refer [join]]
            [hatti.utils.style :refer [answer->color
                                       customizable-style?
                                       get-css-rule-map
                                       group-user-defined-colors-by-answer
                                       group-user-defined-styles-by-answer
                                       qualitative-palette
                                       user-customizable-styles]]))

(def customizable-style [(first user-customizable-styles)
                         "style-definition"])
(def non-customizable-style ["non-customizable" "style-definition"])

(def appearance-attribute
  (join ";" [(join ":" customizable-style)
             (join ":" non-customizable-style)]))

(def field-name "field-name")
(def answer-name "answer-name")

(def css-rule-map
  {(-> customizable-style first keyword) "style-definition"})

(def answers
  [{:appearance "color:green;font-weight:bold;"
    :name "good"}
   {:appearance "color:red;"
    :name "bad"}])

(def field {:name field-name
            :full-name field-name
            :type "select one"
            :children answers})

(deftest test-customizable-style?
  (testing "returns whether a style representation is customizable"
    (is (customizable-style? customizable-style))
    (is (not (customizable-style? non-customizable-style)))))

(deftest test-get-css-rule-map
  (testing "returns a mapping of name to approved styles"
    (is (= (get-css-rule-map appearance-attribute)
           css-rule-map))))

(deftest test-group-user-defined-styles-by-answer
  (testing "returns a map of user-defined styles grouped by answer"
    (let [result {"good" {:color "green"}
                  "bad" {:color "red"}}]
      (is (= (group-user-defined-styles-by-answer field)
             result)))))

(deftest test-group-user-defined-colors-by-answer
  (testing "returns a map of answer to color"
    (is (= (group-user-defined-colors-by-answer field)
           {"good" "green" "bad" "red"}))))

(deftest test-answer->color
  (testing "returns a map of answer to color if field is select-one
            and all children have an appearance attribute"
    (is (= (answer->color field answers)
           {"good" "green" "bad" "red"})))
  (testing "returns a map of answer to color based on the qualitative palette
            if the choices do not all have appearance attributes"
    (let [answers-to-test (assoc-in answers [1 :appearance] nil)
          field-to-test (assoc field :children answers-to-test)
          [good-choice bad-choice] answers-to-test
          [good-color bad-color] qualitative-palette]
      (is (= (answer->color field-to-test answers-to-test)
             {good-choice good-color bad-choice bad-color})))))
