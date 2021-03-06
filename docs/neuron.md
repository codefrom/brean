# Neuron
Most interesting and complex cells of a brain.
Mainly consists of:
* dendrites (D) - input branches of neuron
* soma (C) - core
* axon (A) - tail, that transmits output signals
* synapses (S) - end terminals, which produces neurotransmitters (there are plenty of different types of them)

Schematic:
```
      D   D            < S
       \ /            < S
    D - C -= A = A = <- S
       / \            < S
      D   D            < S
```

## Classification
### Structural 
There are some types of neurons:
* unipolar - 1 axon **or** 1 dendrite 
* bipolar - 1 axon **and** 1 dendride
* multipolar - 1 axon and 2 or more dendrites, there are:
** with long axon
** with short axon
* anaxonic - axon and dendrite are indestinguishable
* pseudounipolar - 1 process, but serves as both axon and dendrite

Oh and there are bunch of "special" ones:
* Basket cells, interneurons that form a dense plexus of terminals around the soma of target cells, found in the cortex and cerebellum
* Betz cells, large motor neurons
* Lugaro cells, interneurons of the cerebellum
* Medium spiny neurons, most neurons in the corpus striatum
* Purkinje cells, huge neurons in the cerebellum, a type of Golgi I multipolar neuron
* Pyramidal cells, neurons with triangular soma, a type of Golgi I
* Renshaw cells, neurons with both ends linked to alpha motor neurons
* Unipolar brush cells, interneurons with unique dendrite ending in a brush-like tuft
* Granule cells, a type of Golgi II neuron
* Anterior horn cells, motoneurons located in the spinal cord
* Spindle cells, interneurons that connect widely separated areas of the brain

### Functional
* afferent - convey information from tissues and organs into the central nervous system and are also called sensory neurons.
* efferent - (motor neurons) transmit signals from the central nervous system to the effector cells.
* interneurons - connect neurons within specific regions of the central nervous system.

## Action on another neurons
Neurons have different actions on other neurons, depending of receptor (!). Receptors can be classified broadly as^
* excitatory (causing an increase in firing rate)
* inhibitory (causing a decrease in firing rate)
* modulatory (causing long-lasting effects not directly related to firing rate)

## Discharge patterns
Neurons can be classified based on their "electrical" activity:
* tonic or regular spiking - some neurons are typically constantly (tonically) active, typically firing at a constant frequency. Example: interneurons in neurostriatum
* phasic or bursting - neurons that fire in bursts are called phasic
* fast spiking - Some neurons are notable for their high firing rates, for example some types of cortical inhibitory interneurons, cells in globus pallidus, retinal ganglion cells

## Neurotransmiters
Those are chemical messengers thats do the job of transmitting signal from one neuron to another in synapse. There are quite a lot of them and they typically do inhibit, excite or modulate.
I will not focus on this for purpose of modelling, because reaction for neurotransmitter is based on receptor and can be different for different neurons/cells.

## Autapse
Neuron can transmit signal to itself from axon to dendrite.

## Dendrites are complex
Different dendrites branches have different conductivity and effect on target neuron.

# Neural coding
https://en.wikipedia.org/wiki/Neural_coding
It is a neuroscience field concerned with characterising the **hypothetical** relationship between the stimulus and the individual or ensemble neuronal responses and the relationship among the electrical activity of the neurons in the ensemble. Based on the theory that sensory and other information is represented in the brain by networks of neurons, it is thought that neurons can encode both digital and analog information.
* Rate coding - The rate coding model of neuronal firing communication states that as the intensity of a stimulus increases, the frequency or rate of action potentials, or "spike firing", increases. Rate coding is sometimes called frequency coding. 
* Temporal coding - Neurons exhibit high-frequency fluctuations of firing-rates which could be noise or could carry information. Rate coding models suggest that these irregularities are noise, while temporal coding models suggest that they encode information
* Population coding - Population coding is a method to represent stimuli by using the joint activities of a number of neurons. In population coding, each neuron has a distribution of responses over some set of inputs, and the responses of many neurons may be combined to determine some value about the inputs.
* Sparse coding - The sparse code is when each item is encoded by the strong activation of a relatively small set of neurons. For each item to be encoded, this is a different subset of all available neurons. In contrast to sensor-sparse coding, sensor-dense coding implies that all information from possible sensor locations is known. 

# Synaptic plasticity
https://en.wikipedia.org/wiki/Synaptic_plasticity
The ability of synapses to strengthen or weaken over time, in response to increases or decreases in their activity.

