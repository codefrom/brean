# Model concepts
## Brain abstractions and simplifications
Brain:
* Big areas (hidbrain, midbrain, etc)
* Smaller areas in big areas (hypothalamus, cerbral cortex, etc)
* Structures in smaller areas (layers, columns, etc)
* All areas and structures made of neurons
* Learning is mainly based on hormones (feedback)

Neurons:
* Consists of different amount of inputs (0..N) and outputs (0..M)
* Neuron can be different types by their firing pattern - firing at constant rate, firing in burst, firing at high rate
* Neurons can be connected to itself
* Connections between neurons are called synapses
* Neuron can affect another in different ways and in different strenght: exite, inhibit, modulation (of firing rate)
* Newborn brain have many more synapses

## Model implementation thoughts
Taken in consideration all above here are some thoughts about implementation.
* In model should be some sort of time simulation - in simple form computation in steps (simulating per quant of a time)
* Dividing in areas could be implemented either over network, either by lazy loading parts of models in memory (it could be somewhat tricky to determine what parts should be loaded)
* Neuron containing some attributes (like type) and list of connections to other neurons with their attributes (like type, strength, etc.)
* Determine outcome of some action could be tricky too (need some actuator on output)
