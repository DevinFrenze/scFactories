s.boot;
// TODO fix amplitude problems

(
  ~scale_frequency = { | min, max, degree |
    var power_of_two = (1 / degree) - 1,
        range = max - min;
    min + (range / 2.pow(power_of_two));
  };

  ~carriers = Dictionary[
    \sine   -> {| freq = 440, phase = 0 | SinOsc.ar(freq, phase) },
    \pulse  -> {| freq = 400, width = 0.5 | Pulse.ar(freq, width) },
    \saw    -> {| freq = 440 | Saw.ar(freq) },
    \blip   -> {| freq = 400, numHarmonics = 200 | Blip.ar(freq, numHarmonics) }
  ];

  ~rlpf = {| in, filter_freq = 440, rq = 1 | RLPF.ar(in, filter_freq.clip(0, 20000), rq) };

  ~amp_adsr = {| attack = 0.0, decay = 0.0, sustain = 1, release = 0.1,
    amp = 0.1, curve = 'lin' |
    Env.new([0, amp, sustain, 0], [attack, decay, release], curve, 2);
  };

  ~filter_adsr = {| freq = 440,
    filter_attack = 0.02, filter_decay = 0.02, filter_sustain = 0.5, filter_release = 0.1,
    filter_peak = 1, filter_curve = 'lin' |
    var peak = ~scale_frequency.(freq, 20000, filter_peak),
      sustain= ~scale_frequency.(freq, peak, filter_sustain);
    Env.new([freq, peak, sustain, freq], [filter_attack, filter_decay, filter_release], filter_curve, 2);
  };

  ~stereo = { | signal, out = 0 | Out.ar(out, (signal) ! 2) };

  ~simple_synth_gen = { |carrier = \saw |
    SynthDef(\simple_ ++ carrier, { | gate = 1, freq = 440 |
      var frequency = freq,
          signal = SynthDef.wrap(~carriers.at(carrier), [], [frequency]),
          env = EnvGen.ar(SynthDef.wrap(~amp_adsr), gate, doneAction: 2),
          filter_env = EnvGen.ar(SynthDef.wrap(~filter_adsr, [], [freq]), gate);
      signal = SynthDef.wrap(~rlpf, [], [signal * env, filter_env]);
      SynthDef.wrap(~stereo, [], [signal]);
    }).add;
  };

  ~simple_synth_gen.(\pulse); 
  ~simple_synth_gen.(\saw); 
  ~simple_synth_gen.(\blip); 
)

(
  var main_pattern = Pbind(
    \tempo, 1.5,
    \scale, Scale.minor,
  ),

  p1 = Pbindf( main_pattern,
    \instrument, \simple_pulse,
    \degree, Pseq([0,1,2,1,2,3,2,1], inf) + 3,
    \root, 0,
    \dur, 0.25,
  ),

  p2 = Pbindf( main_pattern,
    \instrument, \simple_saw,
    \degree, Pseq([5,3,-1,2], inf) - 16,
    \root, 0,
    \dur, 4,
    \rq, 0.1,
    \filter_sustain, 0.3,
    \filter_peak, 0.2,
  ),

  p3 = Pbindf( main_pattern,
    \instrument, \simple_blip,
    \degree, Pseq([0,1,2,1,3,2,1,2], inf),
    \root, 0,
    \dur, Pseq([0.5, Pn(1,6), 1.5], inf) / 2,
    \rq, 0.2,
    \filter_sustain, 0.5,
    \filter_peak, 0.7,
    \amp, 2,
  ),

  a = Pbind(
    \filter_attack, 0.1,
    \filter_decay, 0.03,
    \filter_sustain, 0.33,
    \filter_peak, 0.2,
    \width, 0.5,
    \rq, 1.5,
  ),

  b = Pbind(
    \filter_attack, 0.1,
    \filter_decay, 0.05,
    \filter_sustain, 0.5,
    \filter_peak, 0.25,
    \width, 0.4,
    \rq, 2,
  );
  
  Ppar([
    p2,
    p3,
    Pchain(a, p1),
    Pchain(b, p1)
  ]).play();
)

(
  var main_pattern = Pbind(
    \tempo, 1.5,
    \scale, Scale.minor,
  ),

  p2 = Pbindf( main_pattern,
    \instrument, \simple_saw,
    \degree, 5 - 16,
    \root, 0,
    \dur, Pseq([1, Pn(0.25, 3), Pn(0.75, 4), 3.25], inf),
    \rq, 0.1,
    \filter_attack, 0.01,
    \filter_decay, 0.05,
    \filter_sustain, 0.5,
    \filter_peak, 0.3,
    \filter_release, 0.1,
  );

  p2.play();
)
