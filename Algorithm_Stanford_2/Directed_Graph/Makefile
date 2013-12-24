CXX = g++
CXXFLAGS = -fast
OBJS = main.o di_graph.o node_heap.o node.o 

prog1 : $(OBJS)
	$(CXX) -o $@ $(OBJS)

main.o : main.cpp di_graph.h
	$(CXX) $(CXXFLAGS) -c main.cpp

di_graph.o: di_graph.cpp node.h di_edge.h
	$(CXX) $(CXXFLAGS) -c di_graph.cpp

node_heap.o: node_heap.cpp node.h
	$(CXX) $(CXXFLAGS) -c node_heap.cpp

node.o: node.cpp di_edge.h
	$(CXX) $(CXXFLAGS) -c node.cpp

clean:
	rm $(OBJS) 
