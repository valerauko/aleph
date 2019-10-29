(ns iny.udp-test
  (:use
    [clojure test])
  (:require
    [manifold.stream :as s]
    [iny.netty :as netty]
    [byte-streams :as bs]
    [iny.udp :as udp]))

(netty/leak-detector-level! :paranoid)

(defmacro with-server [server & body]
  `(let [server# ~server]
     (try
       ~@body
       (finally
         (.close ^java.io.Closeable server#)))))

(def words (slurp "test/words"))

(deftest test-echo
  (let [s @(udp/socket {:port 10001, :epoll? true})]
    (s/put! s {:host "localhost", :port 10001, :message "foo"})
    (is (= "foo"
          (bs/to-string
            (:message
              @(s/take! s)))))
    (s/close! s)))
