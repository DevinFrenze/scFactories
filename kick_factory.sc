midicps(16); // E, 20.6hz
midicps(24); // C, 32.7hz, lowest valid freq for kick ?
midicps(36); // C, 65.4hz, highest valid freq for kick ?

(
  ~kickFactoryFunction = { | carrier, envelope |
    { | gate = 1 |
      var env = EnvGen.ar(SynthDef.wrap(~envelopes.at(envelope)), gate, doneAction: 2),
          frequency = EnvGen.ar(SynthDef.wrap(~frequencyEnvelopes.at(\decay)), gate),
          signal = SynthDef.wrap(~carriers.at(carrier), [], [frequency]);
      signal * env;
    }
  };

  ~kickFactory = { | name = \kick, carrier = \sine, envelope = \adsr |
    SynthDef(name, {
      var signal = SynthDef.wrap(~kickFactoryFunction.(carrier, envelope));
      SynthDef.wrap(~outputs.at(\stereo), [], [signal]);
    }).add;
  };

  "successfully initialized kick factory";
)

~kickFactory.();

(
  Pbind(
    \instrument, \kick,
    \midinote, 30,
    \freq_peak, midicps(Pkey(\midinote) + 24),
    \tempo, 1,
    \dur, 0.3,
    \delta, 1,
    \sustain_level, 1,
  ).play();
)
