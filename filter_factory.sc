( // must run the dictionaries.sc script first

  ~filterFactoryFunction = { | carrier, envelope, filter |
    {
      var sig = SynthDef.wrap(~factoryFunction.(carrier, envelope)); 
      SynthDef.wrap(~filters.at(filter), [], [sig]);
    };
  };

  ~filterFactory = { | name = \synth, carrier = \sine, envelope = \adsr, filter = \lpf |
    SynthDef(name, {
      var signal = SynthDef.wrap(~filterFactoryFunction.(carrier, envelope, filter));
      SynthDef.wrap(~outputs.at(\stereo), [], [signal]);
    }).add;
  };

  ~filterEnvelopeFactoryFunction = { | carrier, envelope, filter |
    { | amp = 0.1, gate = 1 |
      var env = amp * EnvGen.ar(SynthDef.wrap(~envelopes.at(envelope)), gate, doneAction: 2),
          signal = SynthDef.wrap(~carriers.at(carrier)),
          filter_freq = EnvGen.ar(SynthDef.wrap(~filterFrequencyEnvelopes.at(\adsr)), gate);
      SynthDef.wrap(~filters.at(filter), [], [signal * env, filter_freq]);
    };
  };

  ~filterEnvelopeFactory = { | name = \synth, carrier = \sine, envelope = \adsr, filter = \lpf |
    SynthDef(name, {
      var signal = SynthDef.wrap(~filterEnvelopeFactoryFunction.(carrier, envelope, filter));
      SynthDef.wrap(~outputs.at(\stereo), [], [signal]);
    }).add;
  };
)


/* examples of how this gating strategy works for
 * synth form, pbind form, and pmono form
 */

s.queryAllNodes;
p.stop;

~filterFactory.( \a, \pulse, \adsr );
~filterEnvelopeFactory.( \a, \pulse, \adsr );
(
p = Pbind(
  \instrument, \a,
  \degree, Pseries(0, 1, inf),
  \dur, 0.25,
  \freq_attack, 0.01,
  \freq_decay, 0.05,
  \peak, 4000,
  \bias, Pkey(\degree),
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
