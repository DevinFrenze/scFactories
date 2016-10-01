(
~fmMain.(\synthA, 3, \pulse, filter: \rlpf);
~fmMain.(\synthB, 5, \sine, filter: \rlpf);
~fmMain.(\synthC, 3, \sine, filter: \lpf);
)

(
  Pbind(
    \instrument, \synth,
    \freq_lfo_range, 0.02,
    \freq_lfo_freq, 4,
    \midinote, 50,
    // \filter_sustain, Pseq([Pgeom(0.2, 1.05, 24)], inf),
    \filter_peak, midicps(Pkey(\midinote) + 48),
    \filter_sustain, 0.5,
    \sustain_level, 0.2,
    \fm_ratio, 0.1,
    \fm_beta, 0.2,
    \rq, 0.2,
    \dur, Pser([1, 1, 2, 4, 3, 3], inf) / 8,
  ).play();
)
(
  var p1, p2, p3, p4, pBase = Pbind(
    \midinote, 45 + Pseq([7, 0, 0, 2, 3, 3], inf),
    \dur, Pseq([0.125, 0.25, 0.25], inf),
    \delta, Pseq([0.125, 0.375, 1.5], inf),
  );

  p1 = Pbindf( pBase,
    \instrument, \synthA,
    \fm_ratio, 2,
    \filter_sustain, 1,
    \amp, 0.14,
  );

  p2 = Pbindf( pBase,
    \instrument, \synthB,
    \midinote, Pkey(\midinote) - 24,
    \filter_sustain, 0.2,
    \sustain_level, 0.4,
    \fm_ratio, 1,
    \fm_beta, 0.2,
    \amp, 0.6,
    \rq, 0.1,
  );

  p3 = Pbindf( pBase,
    \instrument, \synthB, 
    \midinote, Pkey(\midinote) + 12,
    // \filter_sustain, 0.2,
    // \sustain_level, 0.4,
    \fm_ratio, 2,
    \fm_beta, 0.3,
    \amp, 0.07,
  );

  p4 = Pbind(
    \instrument, \synthC,
    \dur, 0.05,
    \delta, 0.5,
    \filter_sustain, 1,
    \midinote, 60,
    \fm_ratio, 4,
    \fm_beta, 2,
    \amp, 0.1,
  );

  Ptpar([
    0, p1, 0, p2, 0, p3, 0.25, p4]
  ).play();
)
