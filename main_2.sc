// v2 -- not optimistic about it

/* things that correspond with envelope

 * LFO delays for both amplitude and frequency
 * some initial amplitude modulation to unsettle
      filter envelope

 * things to correlate with velocity
 * attack time ?
 * frequency (envelope) of filter
 * resonance of filter
 * vibrato (controlled by AR filter ? 5 hz ?)
 * tremolo (also controlled by AR filter ?)

 * amount of noise

 */

/* think about

 * making the latter part of the sound brighter, not the attack
 * for brass instruments, modulate the filter during the attack portion of the sound
 *    (triangle wave)

 */

 ~filterLevels.at(\adsr).(440, 20000, 0.5);
(
  ~main2 = { | carrier = \pulse, envelope = \adsr, filter = \rlpf |
    SynthDef(\main2, { | gate = 1, freq = 440, amp = 0.1, curve = \lin |
      var times = SynthDef.wrap(~envTimes.at(envelope)),
          levels       = SynthDef.wrap(~envLevels.at(envelope)),
          filterLevels = SynthDef.wrap(~filterLevels.at(envelope), [], [freq]),
          env = Env.new(levels, times, curve, 2),
          attackEnv = Env.new([0, 1, 0, 0], times, curve, 2),
          filterEnv = Env.new(filterLevels, times, curve, 2),
          delayTimes = SynthDef.wrap(~envTimes.at(\dasr), [], [times[0] + times[1], times[0], times[2]]),
          delayLevels = SynthDef.wrap(~envLevels.at(\dasr), [], [0, 1]), 
          delayEnv = Env.new(delayLevels, delayTimes, curve, 2),
          ampLfo = SynthDef.wrap(~ampLfo.(\sine), [], [amp]), 
          freqLfo = SynthDef.wrap(~freqLfo.(\sine), [], [freq]), 
          signal;
      env       = EnvGen.ar(env, gate, doneAction: 2);
      filterEnv = EnvGen.ar(filterEnv, gate, doneAction: 0);
      delayEnv  = EnvGen.ar(delayEnv, gate, doneAction: 0);
      attackEnv  = EnvGen.ar(attackEnv, gate, doneAction: 0);
      ampLfo = delayEnv * ampLfo;
      freqLfo = delayEnv * freqLfo;
      signal = SynthDef.wrap(~carriers.at(carrier), [], [freq + freqLfo]);
      signal = signal + (amp * attackEnv * SynthDef.wrap(~noise.at(\gray)));
      signal = SynthDef.wrap(~filters.at(filter), [], [signal, filterEnv]);
      SynthDef.wrap(~outputs.at(\stereo), [], [signal * env, amp + ampLfo]);
    }).add;
  };
)

~main2.(\saw);

(
  Pbind(
    \instrument, \main2,
    \midinote, 40,
    \sustain_level, 1,
    \attack, 0.1,
    \decay, 0.02,
    \dur, 0.75,
    \delta, 1,
    \filter_sustain, 0.8,
    \amp_lfo_range, 0.3,
    \filter_peak, 3000,
    \noise, 0.5,
    \rq, 1,
  ).play();
)
