(
  ~sine = {| freq = 440, phase = 0 | SinOsc.ar(freq, phase) };

  ~stereoOut = { | signal, amp = 0.1, out = 0 | Out.ar(out, (signal * amp) ! 2) };

  ~adsr = {| attack = 0.05, decay = 0.05, sustain_level = 0.1, release = 0.1, curve = 'lin' |
    Env.adsr(attack, decay, sustain_level, release, curve: curve)
  };

  SynthDef(\sine, { | gate = 1 |
    var env = EnvGen.ar(SynthDef.wrap(~adsr), gate, doneAction: 2),
        signal = SynthDef.wrap(~sine, [0.05]);
    SynthDef.wrap(~stereoOut, [nil, 0.05], [signal * env]);
  }).add;

  "successfully added sine synth";
)

s.controlrate;

Synth(\sine);
x = Synth(\sine);
x.set(\amp, 0.1);
x.release;
x.free;
x;
