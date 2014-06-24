(ns cav.core
  (:require [clojure.walk :refer [prewalk postwalk]]))

(defn split
  "Split a coll into n pieces. The inverse of the interleave function."
  [n coll]
  (->> (partition n coll)
       (apply map vector)))

(defn mapi
  "Like clojure.core/map, but with index."
  [f coll]
  (->> (map list (range) coll)
       (map (fn [[k v]] (f k v)))))

(defn filteri
  "Like clojure.core/filter, but with key value pair."
  [pred coll]
  (->> (map list (range) coll)
       (filter (fn [[k v]] (pred k v)))
       (map (fn [[k v]] v))))


;; Macros for Clojure.

#+clj
(defmacro def-
  "A (def ^:private ...)"
  [name expr]
  (list `def (with-meta name {:private true}) expr))

#+clj
(defmacro memo-fn
  "A memoized loop."
  [name bindings & body]
  `(let [cache# (atom {})]
     (fn ~name ~bindings
       (if-let [res# (cache# ~bindings)]
         res#
         (let [res# ~@body]
           (swap! cache# assoc ~bindings res#)
           res#)))))

;; (defmacro flat-lets
;;   "Flat let, as opposed to hierchical default let."
;;   [& body]
;;   (cons `do
;;         (postwalk
;;           (fn [expr]
;;             (if (seq? expr)
;;               (loop [s expr
;;                      cont identity]
;;                 (match (first s)
;;                   ;; guard with seq? because of postwalk's strange behavior of making
;;                   ;; lists seqs.
;;                   ((['let name value] :seq) :guard seq?) (recur (rest s)
;;                                                                 #(cont `((let [~name ~value] ~@%))))
;;                   ((['let & _] :seq) :guard seq?) (throw (IllegalArgumentException. "Should be: (let name value)"))
;;                   nil (cont s)
;;                   :else (recur (rest s)
;;                                #(cont (cons (first s) %)))))
;;               expr))
;;           body)))
