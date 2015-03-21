(ns clj-fdb.core)

(defn tuple
  [ & parts ]
  (.pack (com.foundationdb.tuple.Tuple/from  (object-array parts)))
  )

(defn afn 
  [body]
     (proxy  [com.foundationdb.async.Function]  []
       (apply  [tr] (body tr)) ))

(defn dbrun
  [db cmd]
  (.run db (afn cmd))
  )

(defn bytes-to-value 
  [byts]
  (.getString (com.foundationdb.tuple.Tuple/fromBytes byts) 0)
  )

(defn bytes-to-tuple 
  [byts]
  (into []  (.getItems (com.foundationdb.tuple.Tuple/fromBytes byts)))
  )

(defn fetch-range 
  ( [tr rnge] ( into [] (.get
                          (.asList  
                            (.getRange tr rnge))))) 
  ( [tr rnge limit is-reverse] ( into [] (.get
                          (.asList  
                            (.getRange tr rnge limit is-reverse))))) 
  ( [tr start end limit is-reverse] ( into [] (.get
                          (.asList  
                            (.getRange tr start end limit is-reverse))))))

(defn fetch-starts-with
  [tuple] 
  (dbrun db (fn [tr] (map #(list (to-tuple (.getKey %)) ( read-string (value-from-bytes (.getValue %)))) 
                          ( fetch-range tr (com.foundationdb.Range/startsWith  (tuple "m" memberid metricid)))))))

