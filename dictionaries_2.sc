s.boot;
s.makeGui;

// UGH i don't think this is gonna stick

(
  ~carriers = Dictionary[
    \sine   -> {| freq = 440, phase = 0 | SinOsc.ar(freq, phase) },
    \pulse  -> {| freq = 400, width = 0.5 | Pulse.ar(freq, width) },
    \saw    -> {| freq = 440 | Saw.ar(freq) },
    \blip   -> {| freq = 400, numHarmonics = 200 | Blip.ar(freq, numHarmonics) }
  ];

  ~noise = Dictionary[
    \white  -> {| noise = 0.05 | WhiteNoise.ar(noise) },
    \pink   -> {| noise = 0.05 | PinkNoise.ar(noise) },
    \brown  -> {| noise = 0.05 | BrownNoise.ar(noise) },
    \gray   -> {| noise = 0.05 | GrayNoise.ar(noise) },
    \clip   -> {| noise = 0.05 | ClipNoise.ar(noise) }
  ];

  //  MODULATORS
  //  the modulators functions are wrapped for use as amplitude and frequency LFOs
  //  TODO make triangle

  ~modulators = Dictionary[
    \sine  -> {| freq, range, offset | SinOsc.ar(freq, 0, range, offset); },
    \pulse -> {| freq, range, offset | LFPulse.ar(freq, 0, 0.5, range, offset); },
    \saw   -> {| freq, range, offset | LFSaw.ar(freq, 0, range, offset); },
    \blip  -> {| freq, range, offset, modulator_numHarmonics = 200 | Blip.ar(freq, modulator_numHarmonics, range, offset); }
  ];

  ~ampLfo = { | modulator = \sine |
    { | amp, amp_lfo_freq = 2, amp_lfo_range = 0 |
      SynthDef.wrap(~modulators.at(modulator), [], [amp_lfo_freq, amp_lfo_range * amp, 0]);
    }
  };

  ~freqLfo = { | modulator = \sine |
    { | freq, freq_lfo_freq = 2, freq_lfo_range = 0 |
      SynthDef.wrap(~modulators.at(modulator), [], [freq_lfo_freq, freq_lfo_range * freq, 0]);
    }
  };

  //  ENVELOPES
  //  the envelopes functions are wrapped for frequencies and filters because the non-time-domain properties will
  //  be different, but the time domain properties (and the curve) are left matching. not sure how to avoid
  //  conflicts yet

  ~envelopes = Dictionary[
    \adsr  -> {| envLevels, envTimes, curve = 'lin' | Env.new(envLevels, envTimes, curve, 2); }
  ];

  ~envTimes = Dictionary[
    \adsr   -> {| attack = 0.02, decay = 0.02, release = 0.1 | [attack, decay, release]; },
    \asr   -> {| attack = 0.02, release = 0.1 | [attack, release]; },
    \dadsr  -> {| delay = 0.04, attack = 0.02, decay = 0.02, release = 0.1 | [delay, attack, decay, release]; },
    \dasr  -> {| delay = 0.04, attack = 0.02, release = 0.1 | [delay, attack, release]; }
  ];

  ~envLevels = Dictionary[
    \adsr -> {| bias = 0, peak = 1, sustain_level = 0.2 | [bias, peak, sustain_level, bias]; },
    \asr -> {| bias = 0, sustain_level = 0.2 | [bias, sustain_level, bias]; },
    \dadsr -> {| bias = 0, peak = 1, sustain_level = 0.2 | [bias, bias, peak, sustain_level, bias]; },
    \dasr -> {| bias = 0, sustain_level = 1 | [bias, bias, sustain_level, bias]; }
  ];

  ~filterLevels = Dictionary[
    \adsr -> {| freq = 440, filter_peak = 20000, filter_sustain = 0.5 |
      [freq, filter_peak, freq + (filter_sustain * (filter_peak - freq)), freq];
    };
  ];

  /*
  ~filterEnvelopes = Dictionary[
    \adsr  -> {| freq, bias = 0, filter_peak = 20000, peak = 1, filter_sustain = 0.5, sustain_level = 0.1, attack = 0.01, decay = 0.01, release = 0.1, curve = 'lin' |
      [
        ~envelopes.at(\adsr).(bias, peak, sustain_level, attack, decay, release, curve),
        ~envelopes.at(\adsr).(freq, filter_peak, filter_sustain, attack, decay, release, curve),
      ]
    },
  ];

  ~frequencyEnvelopes = { | envelope = \adsr |
    { | freq, freq_peak, freq_sustain = 0 |
      SynthDef.wrap(~envelopes.at(envelope), [], [freq, freq_peak, freq_sustain]);
    }
  };

  ~frequencyEnvelopes = Dictionary[
    \adsr  -> {| freq, bias = 0, freq_peak = 20000, peak = 1, freq_sustain = 0, sustain_level = 0.1, attack = 0.01, decay = 0.01, release = 0.1, curve = 'lin' |
      [
        ~envelopes.at(\adsr).(bias, peak, sustain_level, attack, decay, release, curve),
        ~envelopes.at(\adsr).(freq, freq_peak, freq_sustain, attack, decay, release, curve),
      ]
    },
  ];
  */

  // example

  ~envelopesFunction = { | envelope = \adsr |
    { | gate = 1, doneAction = 2 |
      var env = EnvGen.ar(SynthDef.wrap(~envelopes.at(envelope)), gate, doneAction: doneAction);
      env;
    };
  };


  //  OUTPUTS
  //  the output functions are super basic right now, but could potentially include spacialization
  //  may even include effects busses or things

  ~outputs = Dictionary[
    \stereo -> { | signal, amp = 0.1, out = 0 | Out.ar(out, (signal * amp) ! 2) },
    \mono   -> { | signal, amp = 0.1, out = 0 | Out.ar(out, signal * amp) },
  ];

  //  FILTERS
  //  the filter functions are generally used with ~filterEnvelopes and are generally used right before output

  ~filters = Dictionary[
    \lpf  -> {| in, filter_freq = 440 | LPF.ar(in, filter_freq.clip(0, 20000)) },
    \hpf  -> {| in, filter_freq = 440 | HPF.ar(in, filter_freq.clip(0, 20000)) },
    \rlpf -> {| in, filter_freq = 440, rq = 1 | RLPF.ar(in, filter_freq.clip(0, 20000), rq) },
    \rhpf -> {| in, filter_freq = 440, rq = 1 | RHPF.ar(in, filter_freq.clip(0, 20000), rq) },
    \bpf  -> {| in, filter_freq = 440, rq = 1 | BPF.ar(in, filter_freq.clip(0, 20000), rq) },
    \brf  -> {| in, filter_freq = 440, rq = 1 | BRF.ar(in, filter_freq.clip(0, 20000), rq) }
  ];

  //  FREQUENCY MODULATION
  //  our function for recursive frequency modulation

  ~fmFunction = { | fm_layers, carrier, modulator |
    { | freq = 440, fm_ratio = 1, fm_beta = 0|
      var mod = freq, signal;

      fm_layers.do {
        mod = (~modulators.at(modulator)).(
          mod,
          mod * fm_ratio * fm_beta,
          mod * fm_ratio
        );
      };

      signal = SynthDef.wrap(~carriers.at(carrier), [], [mod]);
    };
  }; 

  "successfully initialized dictionaries 2.0";
)
