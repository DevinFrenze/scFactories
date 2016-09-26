/*
 * in synth definitions, use gate instead of dur
 * when using a synth in a pattern, supply dur instead of gate
 *
 * note: not sure how to retrigger envelopes for pmono
 */

( // must run the dictionaries.sc script first
  ~envelopeFactoryFunction = { | envelope = \adsr |
    { | gate = 1, doneAction = 2 |
      var env = EnvGen.ar(SynthDef.wrap(~envelopes.at(envelope)), gate, doneAction: doneAction);
      env;
    };
  };

  ~factoryFunction = { | carrier, envelope |
    { var env = SynthDef.wrap(~envelopeFactoryFunction.(envelope)),
          signal = SynthDef.wrap(~carriers.at(carrier));
      signal * env;
    };
  }; 

  ~factory = { | name = \synth, carrier = \sine, envelope = \adsr |
    SynthDef(name, {
      var signal = SynthDef.wrap(~factoryFunction.(carrier, envelope));
      SynthDef.wrap(~outputs.at(\stereo), [], [signal]);
    }).add;
  };
  "successfully initialized basic factory";
)


/* examples of how this gating strategy works for
 * synth form, pbind form, and pmono form
 */

s.queryAllNodes;
p.stop;

~factory.( \a, \pulse, \dadsr );
(
p = Pbind(
  \instrument, \a,
  \freq, 880,
  \dur, 0.25,
  \delta, 0.5,
).play; 
)

(
p = Pbind(
  \instrument, \a,
  \freq, 880,
  \dur, 0.25
).play; 

p = Pbind(
  \instrument, \a,
  \freq, 880,
  \dur, 0.25,
  \delay, 0.05
).play; 

p = Pbind(
  \instrument, \a,
  \freq, 880,
  \dur, 0.25,
  \delay, 0.1
).play; 
)

(
p = Pmono(
  \a,
  \degree, Pseries(0, 1, inf),
  \dur, 0.25
).play; 
)

x = Synth(\a);
x.set(\gate, 0);
