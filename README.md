# SuperCollider Factory Workflow

Trying to improve my SuperCollider workflow so that when it comes time to write
music I do less instrument building and more experimenting. The intended
workflow consists of a dictionaries, synth factories, and pattern /
midi utilities.

## Dictionaries

In SuperCollider dictionaries are key value pairs, and in _these_ dictionaries
the keys are symbols and the values are functions. Each dictionary contains some
number of simple functions that can be used for a similar purpose. For instance,
there is a dictionary of carrier waveforms (sine, saw, ...), any of which could
be used as a source signal. There is also a dictionary of envelopes, any of
which could be used to contour a signal.

## Factories

Factories are the centerpiece of the workflow. A factory is a function that
dynamically constructs a SynthDef and adds it to our server. Each factory is an
arrangement of dictionaries and other factories that specifies the nature of
the SynthDefs it can create.

For example, take our basic factory in `basic_factory.sc`. The user can invoke
the factory with a name and a variety of options specifying how it should
construct a SynthDef. In the basic factory, these options are `carrier` and
`envelope`. So the user can choose any carrier that exists in the carrier
dictionary, and any envelope from the envelope dictionary, and pass their keys
to the factory, and the factory will construct a synth definition using those
functions.

## Patterns & MIDI

Once a SynthDef is generated, now we want to hear it make sound! The two main
ways that happens is through Patterns (Pbind, Pmono ...) and through MIDI
instruments. It's important that there are pattern utilities that encapsulate
common patterns, such as linking a filter frequency to the primary frequency.
It's just as important that there are reusable tools for MIDI performance such
as functions that handle whether a synth should be played as monophonic,
duophonic, or multiphonic, or whether it should have lowest-note-priority,
highest-note-priority, last-note-priority, or first-note-priority.

### Also
It would be nice to include a standard start file and probably other things will
come up that will be nice to include. If you're reading this and you're on
OSX, definitely check out my scvim branch so that you can use vim as your text
editor instead of the SuperCollider IDE (if you want).
