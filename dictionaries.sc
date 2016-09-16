s.boot;

(
  ~carriers = Dictionary[
    \sine   -> {| freq = 440, phase = 0 | SinOsc.ar(freq, phase) },
    \pulse  -> {| freq = 400, width = 0.5 | Pulse.ar(freq, width) },
    \saw    -> {| freq = 440 | Saw.ar(freq) },
    \blip   -> {| freq = 400, numHarmonics = 200 | Blip.ar(freq, numHarmonics) }
  ];

  // need to use sustained envelopes because of the way we're using gate
  ~envelopes = Dictionary[
    \adsr  -> {| attack = 0.01, decay = 0.01, sustain = 0.1, release = 0.1, curve = 'lin' |
      Env.adsr(attack, decay, sustain, release, curve: curve)
    },
    \dadsr  -> {| delay = 0, attack = 0.01, decay = 0.01, sustain = 0.1, release = 0.1, curve = 'lin' |
      Env.dadsr(delay, attack, decay, sustain, release, curve: curve)
    },
    \asr  -> {| attack = 0.01, sustain = 0.1, release = 0.1, curve = 'lin' |
      Env.asr(attack, sustain, release, curve)
    },
    \cutoff  -> {| release = 0.1, curve = 'lin' |
      Env.cutoff(release, curve: curve)
    }
  ];

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

  ~outputs = Dictionary[
    \stereo -> { | signal, out = 0 | Out.ar(out, signal ! 2) },
    \mono   -> { | signal, out = 0 | Out.ar(out, signal) },
  ];

  ~filters = Dictionary[
    \lpf -> {| in, filter_freq = 440 | LPF.ar(in, filter_freq) },
    \hpf -> {| in, filter_freq = 440 | HPF.ar(in, filter_freq) },
    \bpf -> {| in, filter_freq = 440, rq = 1 | BPF.ar(in, filter_freq, rq) },
    \brf -> {| in, filter_freq = 440, rq = 1 | BRF.ar(in, filter_freq, rq) }
  ];
)
