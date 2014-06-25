(ns cav.core
  (:require [clojure.walk :refer [prewalk postwalk]]))

(defn split
  "Split a coll into n pieces. The inverse of the interleave function."
  [n coll]
  (->> (partition n coll)
       (apply map vector)))

(defn mapi
  "Like clojure.core/map, but with index."
  ([f coll] (mapi f 0 coll))
  ([f i coll]
   (lazy-seq
     (when-let [c (seq coll)]
       (cons (f i (first coll)) (mapi f (inc i) (rest coll)))))))

(defn filteri
  "Like clojure.core/filter, but with index."
  ([pred coll] (filteri pred 0 coll))
  ([pred i coll]
   (lazy-seq
     (when-let [c (seq coll)]
       (let [v (first coll)]
         (if (pred i v)
           (cons v (filteri pred (inc i) (rest coll)))
           (filteri pred (inc i) (rest coll))))))))

(defn removei
  "Like clojure.core/remove, but with index."
  ([pred coll] (removei pred 0 coll))
  ([pred i coll] (filteri (complement pred) i coll)))

(defn rm
  "Like clojure.core/remove, but returns vector if coll is vector."
  [pred coll]
  (let [removed (remove pred coll)]
    (if (vector? coll)
      (vec removed)
      removed)))

(defn rmi
  "Like cav.core/rm, but with index."
  ([pred coll] (rmi pred 0 coll))
  ([pred i coll]
   (let [removed (removei pred i coll)]
     (if (vector? coll)
       (vec removed)
       removed))))


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
