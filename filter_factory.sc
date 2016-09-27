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
    { | gate = 1 |
      var env = EnvGen.ar(SynthDef.wrap(~envelopes.at(envelope)), gate, doneAction: 2),
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

  "successfully initialized filter factories";
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
  \midinote, Pseries(60, 1, inf),
  \dur, 0.15,
  \delta, 0.25,
  \freq_attack, 0.11,
  \freq_decay, 0.05,
  // \freq_peak, midicps(Pkey(\midinote) + 65),
  \freq_bias, midicps(Pkey(\midinote) + 5),
  \amp, 2,
).play; 
)
