s.boot;

(
  ~carriers = Dictionary[
    \sine   -> {| freq = 440, phase = 0 | SinOsc.ar(freq, phase) },
    \pulse  -> {| freq = 400, width = 0.5 | Pulse.ar(freq, width) },
    \saw    -> {| freq = 440 | Saw.ar(freq) },
    \blip   -> {| freq = 400, numHarmonics = 200 | Blip.ar(freq, numHarmonics) }
  ];

  ~modulators = Dictionary[
    \sine  -> {| freq, range, offset, phase = 0 | SinOsc.ar(freq, phase, range, offset); },
    \pulse -> {| freq, range, offset, pulse_width = 0.5 | LFPulse.ar(freq, 0, pulse_width, range, offset); },
    \saw   -> {| freq, range, offset | LFSaw.ar(freq, 0, range, offset); },
    \blip  -> {| freq, range, offset, numHarmonics = 200 | Blip.ar(freq, numHarmonics, range, offset); }
  ];

  // need to use sustained envelopes because of the way we're using gate
  ~envelopes = Dictionary[
    \adsr  -> {| attack = 0.01, decay = 0.01, sustain_level = 0.1, release = 0.1, curve = 'lin' |
      Env.adsr(attack, decay, sustain_level, release, curve: curve)
    },
    \dadsr  -> {| delay = 0, attack = 0.01, decay = 0.01, sustain_level = 0.1, release = 0.1, curve = 'lin' |
      Env.dadsr(delay, attack, decay, sustain_level, release, curve: curve)
    },
    \asr  -> {| attack = 0.01, sustain_level = 0.1, release = 0.1, curve = 'lin' |
      Env.asr(attack, sustain_level, release, curve)
    },
    \cutoff  -> {| release = 0.1, curve = 'lin' |
      Env.cutoff(release, curve: curve)
    }
  ];

  ~envelopeFactoryFunction = { | envelope = \adsr |
    { | gate = 1, doneAction = 2 |
      var env = EnvGen.ar(SynthDef.wrap(~envelopes.at(envelope)), gate, doneAction: doneAction);
      env;
    };
  };

  ~filterFrequencyEnvelopes = Dictionary[
    \adsr  -> {| freq_attack = 0.01, freq_decay = 0.01, freq_sustain = 0.1, freq_release = 0.1, freq_peak = 20000, freq_curve = 'lin', freq_bias = 440 |
      Env.adsr(freq_attack, freq_decay, freq_sustain, freq_release, freq_peak, freq_curve, freq_bias)
    },
    \dadsr  -> {| freq_delay = 0, freq_attack = 0.01, freq_decay = 0.01, freq_sustain = 0.1, freq_release = 0.1, freq_peak = 20000, freq_curve = 'lin', freq_bias = 440 |
      Env.dadsr(freq_delay, freq_attack, freq_decay, freq_sustain, freq_release, freq_peak, freq_curve, freq_bias)
    },
    \asr  -> {| freq_attack = 0.01, freq_sustain = 0.1, freq_release = 0.1, freq_peak = 20000, freq_curve = 'lin', freq_bias = 440 |
      Env.asr(freq_attack, freq_sustain, freq_release, freq_curve) * (freq_peak - freq_bias) + freq_bias
    },
    \cutoff  -> {| freq_release = 0.1, freq_peak = 20000, freq_curve = 'lin', freq_bias = 440 |
      Env.cutoff(freq_release, curve: freq_curve) * (freq_peak - freq_bias) + freq_bias
    }
  ];

  ~frequencyEnvelopes = Dictionary[
    \adsr  -> {| freq_attack = 0.01, freq_decay = 0.01, freq_sustain = 0.1, freq_release = 0.1, freq_peak = 20000, freq_curve = 'lin', freq = 440 |
      Env.adsr(freq_attack, freq_decay, freq_sustain, freq_release, freq_peak, freq_curve, freq)
    },
    \dadsr  -> {| freq_delay = 0, freq_attack = 0.01, freq_decay = 0.01, freq_sustain = 0.1, freq_release = 0.1, freq_peak = 20000, freq_curve = 'lin', freq = 440 |
      Env.dadsr(freq_delay, freq_attack, freq_decay, freq_sustain, freq_release, freq_peak, freq_curve, freq)
    },
    \asr  -> {| freq_attack = 0.01, freq_sustain = 0.1, freq_release = 0.1, freq_peak = 20000, freq_curve = 'lin', freq = 440 |
      Env.asr(freq_attack, freq_sustain, freq_release, freq_curve) * (freq_peak - freq) + freq
    },
    \cutoff  -> {| freq_release = 0.1, freq_peak = 20000, freq_curve = 'lin', freq = 440 |
      Env.cutoff(freq_release, curve: freq_curve) * (freq_peak - freq) + freq
    },
    \decay -> {| freq_decay = 0.05, freq_release, freq_peak = 20000, freq_curve = 'lin', freq = 440 |
      Env.adsr(0, freq_decay, 0, freq_release, freq_peak, freq_curve, freq)
    }
  ];

  ~frequencyEnvelopeFactoryFunction = { | envelope = \adsr |
    { | gate = 1, doneAction = 0 |
      var env = EnvGen.ar(SynthDef.wrap(~filterEnvelopes.at(envelope)), gate, doneAction: doneAction);
      env;
    };
  };

  ~outputs = Dictionary[
    \stereo -> { | signal, out = 0, amp = 0.1 | Out.ar(out, (signal * amp) ! 2) },
    \mono   -> { | signal, out = 0, amp = 0.1 | Out.ar(out, signal * amp) },
  ];

  ~filters = Dictionary[
    \lpf -> {| in, filter_freq = 440 | LPF.ar(in, filter_freq.clip(0, 20000)) },
    \hpf -> {| in, filter_freq = 440 | HPF.ar(in, filter_freq.clip(0, 20000)) },
    \bpf -> {| in, filter_freq = 440, rq = 1 | BPF.ar(in, filter_freq.clip(0, 20000), rq) },
    \brf -> {| in, filter_freq = 440, rq = 1 | BRF.ar(in, filter_freq.clip(0, 20000), rq) }
  ];

  "successfully initialized dictionaries";
)
