(ns cav.algo-test
  (:require [cav.algo :refer [max-sub fib fib-seq]]
            #+clj [clojure.test :refer :all]
            #+cljs [cemerick.cljs.test])
  #+cljs (:require-macros [cemerick.cljs.test :refer [is deftest testing with-test]]))

(deftest max-sub-test
  (testing "Max sub array."
    (let [[[start end] max-val] (max-sub [-1 2 1 -4 0 3 1 2])]
      (is (= 5 start))
      (is (= 7 end))
      (is (= 6 max-val)))))

(deftest fib-test
  (testing "fib 6"
    (is (= 8 (fib 6))))
  
  (testing "fib 0"
    (is (= 0 (fib 0))))

  (testing "fib 1"
    (is (= 1 (fib 1))))

  #+clj
  (testing "fib -1"
    (is (thrown? AssertionError (fib -1)))))

(deftest fib-seq-test
  (testing "inifite fib serie."
    (is (= 10 (count (take 10 (fib-seq)))))))
