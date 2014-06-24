(ns cav.shell)

(def spawn (.-spawn (js/require "child_process")))

(defprotocol IShellCommand
  (read [command callback])
  (read-err [command callback])
  (write [command content])
  (end [command] [command content]))

(defrecord Command [in out err]
  IShellCommand
  (read [_ callback]
    (let [buffer (atom nil)]
      (.on out "readable" #(when-let [readed (.read out)]
                             (swap! buffer str readed)))
      (.once out "end" (fn []
                         (.removeAllListeners out "readable")
                         (callback nil @buffer)))))
  (read-err [_ callback]
    (let [buffer (atom nil)]
      (.on err "readable" #(when-let [readed (.read err)]
                             (swap! buffer str readed)))
      (.once err "end" (fn []
                         (.removeAllListeners err "readable")
                         (callback nil @buffer)))))
  (write [_ content]
    (.write in content))
  (end [_] (.end in))
  (end [_ content] (.end in content)))

(defn command
  "Spawn a shell command."
  ([cmd] (command cmd {}))
  ([cmd opts]
   (let [opts (merge-with
                (fn [l r]
                  (cond
                    (object? r) (goog/mixin l r)
                    (array? r) (.concat l r)
                    :else r))
                {:env (.-env js/process)}
                opts)
         p (spawn "/bin/bash" #js ["-c" cmd] (clj->js opts))]
     (when (not= (:enc opts) "buffer")
       (.. p -stdout (setEncoding "utf8"))
       (.. p -stderr (setEncoding "utf8")))
     (Command. (.-stdin p) (.-stdout p) (.-stderr p)))))
