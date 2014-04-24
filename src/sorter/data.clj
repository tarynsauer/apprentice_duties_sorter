(ns sorter.data)

(def duties [{:name "chairs", :min-needed 2, :max-needed 3} 
             {:name "av", :min-needed 2, :max-needed 3} 
             {:name "food", :min-needed 2, :max-needed 3} 
             {:name "greeter", :min-needed 1, :max-needed 1}
             {:name "standup", :min-needed 1, :max-needed 2}])

(def apprentices [{:name "Taryn", :prev-duty "food", :av-count 3} 
                  {:name "Tom", :prev-duty "av", :av-count 3} 
                  {:name "LaToya", :prev-duty "chairs", :av-count 1} 
                  {:name "Zach O", :prev-duty "greeter", :av-count 1} 
                  {:name "Elizabeth", :prev-duty "food", :av-count 3} 
                  {:name "Meagan", :prev-duty "av", :av-count 3} 
                  {:name "Zach D", :prev-duty "chairs", :av-count 1} 
                  {:name "Mike K", :prev-duty "greeter", :av-count 1} 
                  {:name "Kyle", :prev-duty "av", :av-count 3} 
                  {:name "Mike D", :prev-duty "chairs", :av-count 1} 
                  {:name "Nelsol", :prev-duty "utility", :av-count 1}
                  {:name "Cassie", :prev-duty "food", :av-count 0}])
