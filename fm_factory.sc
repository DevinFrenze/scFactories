(
  ~fmFactoryFunction = { | fm_layers, carrier, envelope, modulator |
    { | freq = 440, fm_ratio = 1, fm_beta = 0|
      var mod = freq, signal;

      fm_layers.do {
        mod = (~modulators.at(modulator)).(
          mod * fm_ratio, 
          mod * fm_ratio * fm_beta,
          mod
        );
      };

      signal = SynthDef.wrap(~carriers.at(carrier), [], [mod]);
    };
  }; 

  ~fmFactory = { | name = \fm, fm_layers = 1, carrier = \sine, envelope = \adsr, modulator = \sine |
    SynthDef(name, {
      var env = SynthDef.wrap(~envelopeFactoryFunction.(envelope)),
          signal = SynthDef.wrap(~fmFactoryFunction.(fm_layers, carrier, envelope, modulator));
      SynthDef.wrap(~outputs.at(\stereo), [], [signal * env]);
    }).add;
  };

  "successfully initialized FM factory";
)

~fmFactory.(\fm, 3);

(
p = Pbind(
  \instrument, \fm,
  \freq, 280,
  \dur, 0.1,
  \delta, 1,
  \amp, 0.2,
  \fm_ratio, 3.4,
  \fm_beta, 2.1,
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
