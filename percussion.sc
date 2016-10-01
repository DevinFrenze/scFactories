(
  SynthDef(\kettle_partial, { | attack = 0.005, decay = 0.1, release = 1, decayLevel = 0.9 |
    var env = EnvGen.ar(Env.new([0, 1, decayLevel, 0], [attack, decay, release]), doneAction: 2),
        signal = SynthDef.wrap(~carriers.at(\sine));
      SynthDef.wrap(~outputs.at(\stereo), [], [env * signal]);
  }).add;
)

(
  var two, three, four,
  one = Pbind(
    \instrument, \kettle_partial,
    \freq, 100,
    \delta, 2,
    \release, 1,
  );

  two = Pbindf(one,
    \freq, Pkey(\freq) * 1.5,
    \release, Pkey(\release) * 1.5,
  );

  three = Pbindf(one,
    \freq, Pkey(\freq) * 1.98,
    \release, Pkey(\release) * 1.5,
  );

  four = Pbindf(one,
    \freq, Pkey(\freq) * 2.44,
    \release, Pkey(\release) * 1.33,
  );

  Ppar([one, two, three, four]).play();
)

FreqShift
