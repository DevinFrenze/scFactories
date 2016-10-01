/* TODO
* amplitude LFO
* pitch lfo w/ envelope
* noise
*/
( // must run the dictionaries.sc script first
  ~main = { | name = \synth, carrier = \sine, envelope = \adsr, filter = \lpf, freq_lfo = \sine |
    SynthDef(name, { | gate = 1, freq = 440 |
      var frequency = SynthDef.wrap(~freqLfoModulator.(freq_lfo), [], [freq]),
          signal = SynthDef.wrap(~carriers.at(carrier), [], [frequency]),
          env = EnvGen.ar(SynthDef.wrap(~envelopes.at(envelope)), gate, doneAction: 2),
          filter_freq = EnvGen.ar(SynthDef.wrap(~filterFrequencyEnvelopes.at(\adsr), [], [freq]), gate),
          amplitude = SynthDef.wrap(~ampLfoModulator.(\sine));
      signal = SynthDef.wrap(~filters.at(filter), [], [signal * env, filter_freq]);
      SynthDef.wrap(~outputs.at(\stereo), [], [signal, amplitude]);
    }).add;
  };

  ~fmMain = { | name = \synth,
      fm_layers = 1,
      carrier = \sine,
      envelope = \adsr,
      filter = \lpf,
      freq_lfo = \sine,
      modulator = \sine
    |
    SynthDef(name, { | gate = 1, freq = 440 |
      // var lfo_signal = SynthDef.wrap(~lfoModulator.(lfo), [], [Lag.kr(freq, 0.5)]),
      var frequency = SynthDef.wrap(~freqLfoModulator.(freq_lfo), [], [freq]),
          signal = SynthDef.wrap(~fmFactoryFunction.(fm_layers, carrier, modulator), [], [frequency]),
          env = EnvGen.ar(SynthDef.wrap(~envelopes.at(envelope)), gate, doneAction: 2),
          filter_freq = EnvGen.ar(SynthDef.wrap(~filterFrequencyEnvelopes.at(\adsr), [], [freq]), gate),
          amplitude = SynthDef.wrap(~ampLfoModulator.(\sine));
      signal = SynthDef.wrap(~filters.at(filter), [], [signal * env, filter_freq]);
      SynthDef.wrap(~outputs.at(\stereo), [], [signal, amplitude]);
    }).add;
  };

  "successfully initialized main generator";
)

~main.(\synth, \pulse);

(
~fmMain.(\synthA, 3, \pulse, filter: \rlpf);
~fmMain.(\synthB, 5, \sine, filter: \rlpf);
~fmMain.(\synthC, 3, \sine, filter: \lpf);
)

s.makeGui;
