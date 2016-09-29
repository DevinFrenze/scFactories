( // must run the dictionaries.sc script first
  ~lfoModulator = { | modulator = \sine |
    { | lfo_freq = 2, lfo_range = 0, freq |
      SynthDef.wrap(~modulators.at(modulator), [], [lfo_freq, lfo_range, freq]);
    }
  };

  ~main = { | name = \synth, carrier = \sine, envelope = \adsr, filter = \lpf, lfo = \sine |
    SynthDef(name, { | gate = 1 |
      var lfo = SynthDef.wrap(~lfoModulator.(lfo)),
          signal = SynthDef.wrap(~carriers.at(carrier), [], [lfo]),
          env = EnvGen.ar(SynthDef.wrap(~envelopes.at(envelope)), gate, doneAction: 2),
          filter_freq = EnvGen.ar(SynthDef.wrap(~filterFrequencyEnvelopes.at(\adsr)), gate);
      // signal = SynthDef.wrap(~filters.at(filter), [], [signal * env, filter_freq]);
      SynthDef.wrap(~outputs.at(\stereo), [], [signal]);
    }).add;
  };

  "successfully initialized main generator";
)

~main.();
