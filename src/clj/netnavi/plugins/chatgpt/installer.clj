; Maybe it shouldn't be installer, but config/setup
(ns netnavi.plugins.chatgpt.installer
  (:require [clojure.java.shell :as shell]))

(defn set-env-var [key value]
  (shell/sh "setx" key value))

(defn notification []
  (println "Please restart this terminal session or IDE for enviornment variables to take effect"))

(defn request-org []
  (print "Enter OPENAI_ORGANIZATION: ")
  (flush)
  (let [api-key (read-line)]
    (set-env-var "OPENAI_ORGANIZATION" api-key)))

(defn request-key []
  (print "Enter OPENAI_API_KEY: ")
  (flush)
  (let [api-key (read-line)]
    (set-env-var "OPENAI_API_KEY" api-key)))

(defn request-api-info []
  (println "There is no environmental variable set for OPENAI_API_KEY and OPENAI_API_ORG. Please set them.")
  (print "Would you like to set them now? (yes/no): ")
  ; Needed, or else print won't display before taking input
  (flush)
  (let [response (read-line)]
    (cond
      (= response "no") (System/exit 0)
      (= response "yes") (do
                           (request-key)
                           (request-org))
      :else (request-api-info))))

(defn do-startup-check []
  (let [checked-variable (System/getenv "OPENAI_API_KEY")]
    (if (nil? checked-variable)
      (request-api-info)
      (println "ChatGPT configuration satisfactory. continuing..."))))

; just an idea
(defn if-not-restarted []
  (slurp "config"))

;(do-startup-check)
;(request-api-info)