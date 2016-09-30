(
  ~fmFactory = { | name = \fm, fm_layers = 1, carrier = \sine, envelope = \adsr, modulator = \sine |
    SynthDef(name, {
      var env = SynthDef.wrap(~envelopeFactoryFunction.(envelope)),
          signal = SynthDef.wrap(~fmFactoryFunction.(fm_layers, carrier, envelope, modulator));
      SynthDef.wrap(~outputs.at(\stereo), [], [signal * env]);
    }).add;
  };

  ~fmFilterFactory = { | name = \fm, fm_layers = 1, carrier = \sine, envelope = \adsr, modulator = \sine, filter \lpf |
    SynthDef(name, {
      | gate = 1 |
      var env = EnvGen.ar(SynthDef.wrap(~envelopes.at(envelope)), gate, doneAction: 2),
          filter_freq = EnvGen.ar(SynthDef.wrap(~filterFrequencyEnvelopes.at(\adsr)), gate),
          signal = SynthDef.wrap(~fmFactoryFunction.(fm_layers, carrier, modulator));
      signal = SynthDef.wrap(~filters.at(filter), [], [signal, filter_freq]);
      SynthDef.wrap(~outputs.at(\stereo), [], [signal * env]);
    }).add;
  };

  "successfully initialized FM factory";
)

~fmFactory.(\fm, 3);
~fmFilterFactory.(\fm, 3);

(
p = Pbind(
  \instrument, \fm,
  \midinote, 40,
  \tempo, 2,
  \dur, 0.15,
  \delta, 0.25,
  \amp, 0.2,
  \fm_ratio, 3.4,
  \fm_beta, 2.1,
  \freq_attack, 0.1,
  \freq_decay, 0.05,
  \freq_curve, \lin,
  \freq_bias, midicps(Pkey(\midinote)),
  \freq_peak, Pseq([10, 20, 10, 15], inf) * 500,
).play; 
)

~fmFactory.(\fm, 7);

(
p = Pbind(
  \instrument, \fm,
  \freq, 280,
  \dur, 0.5,
  \delta, 1,
  \amp, 0.1,
  \fm_ratio, 1.25,
  \fm_beta, 0.65,
  \attack, 0.05,
  \decay, 0.05,
  \sus_level, 0.1,
  \release, 0.02,
).play; 
)
