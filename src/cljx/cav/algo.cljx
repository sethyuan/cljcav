(ns cav.algo)

(defn max-sub
  "Find the max subarray and its sum."
  [coll]
  (loop [start -1
         end -1
         max-ending 0
         max-so-far 0
         i 0
         coll coll]
    (if (seq coll)
      (let [ending (max 0 (+ max-ending (first coll)))]
        (recur
          (if (and (zero? max-ending) (>= ending max-so-far)) i start)
          (if (> ending max-so-far) i end)
          ending
          (max max-so-far ending)
          (inc i)
          (rest coll)))
      [[start end] max-so-far])))

(defn fib
  "Find the specified fibonacci number."
  [#+clj ^long n #+cljs n]
  {:pre [(>= n 0)]}
  (case n
    0 0
    1 1
    (loop [p1 0
           p2 1
           i 2]
      (let [v (+ p1 p2)]
        (if (< i n)
          (recur p2 v (inc i))
          v)))))

(defn fib-seq
  "Get an infinite fibonacci serie seq."
  []
  (let [fib (fn fib [f1 f2]
              (lazy-seq (cons (+ f1 f2) (fib f2 (+ f1 f2)))))]
    (cons 0 (cons 1 (fib 0 1)))))
