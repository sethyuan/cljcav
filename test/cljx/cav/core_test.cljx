(ns cav.core-test
  (:require [cav.core :as cav]
            #+clj [clojure.test :refer :all]
            #+cljs [cemerick.cljs.test])
  #+cljs (:require-macros [cemerick.cljs.test :refer [is deftest testing with-test]]))

(deftest split-test
  (testing "split 2."
    (let [[f s] (cav/split 2 (range 1 11))]
      (is (= [1 3 5 7 9] f))
      (is (= [2 4 6 8 10] s))))
  
  (testing "split 3."
    (let [[f s t] (cav/split 3 (range 1 11))]
      (is (= [1 4 7] f))
      (is (= [2 5 8] s))
      (is (= [3 6 9] t)))))

(deftest mapi-test
  (testing "dashed."
    (is (= ["item-0" "ok-1"] (cav/mapi #(str %2 "-" %1) ["item" "ok"])))))

(deftest filteri-test
  (testing "second elements."
    (is (= [1 3 5] (cav/filteri (fn [i _] (= (rem i 2) 1))
                              (range 6))))))

(deftest removei-test
  (testing "simple removei"
    (is (= '(0 1 3)
           (cav/removei (fn [i _] (= i 2)) (range 4))))))

(deftest rm-test
  (testing "simple rm"
    (is (= '(0 1 3)
           (cav/rm #{2} (range 4))))))

(deftest rmi-test
  (testing "simple rmi"
    (is (= '(0 1 3)
           (cav/rmi (fn [i v] (= (+ i v) 4)) (range 4))))))

;; (deftest flat-lets-test
;;   (testing "simple."
;;     (is (= (flat-lets
;;              (let x 99)
;;              x)
;;            99)))
;;
;;   (testing "double set."
;;     (is (= (flat-lets
;;              (let x 99)
;;              (let x (inc x))
;;              x)
;;            100)))
;;
;;   (testing "multiple vals."
;;     (is (= (flat-lets
;;              (let [x y z] [1 2 3])
;;              (+ x y z))
;;            6)))
;;
;;   (testing "inner let."
;;     (is (= (flat-lets
;;              (do
;;                (let [x y z] [1 2 3])
;;                (+ x y z)))
;;            6)))
;;
;;   #_(testing "vector instead of list."
;;     (is (thrown? Exception
;;                  (flat-lets
;;                    [let x 1]
;;                    x))))
;;
;;   #_(testing "wrong let."
;;     (is (thrown? IllegalArgumentException
;;                  (flat-lets
;;                    (let x)
;;                    x)))))
