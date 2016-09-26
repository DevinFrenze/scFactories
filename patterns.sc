~filterEnvelopeFactory.( \a, \pulse, \adsr );
~factory.( \b, \sine, \adsr );
~kickFactory.();

// an example where the streams are constructed modularly
// reused in multiple instruments, and sequenced in phrases

(
  var rhythm, part1, part2, parts, instrument1, instrument2, kick;

  rhythm = Pbind(
    \tempo, 2.4,
    \dur, 0.2,
    \delta, Pseq([1, 0.5, 0.25, Pn(0.75, 3)], inf),

  );

  part1 = Pbindf(rhythm, \midinote, 77 + Pseq([Pn(4, 3), Pn(0, 6), Pn(2, 3)], 3));

  part2 = Pbindf(rhythm, \midinote, 77 + Pseq([7, 9, 7, 5, 4, 2, Pn(0, 3), 4, 2, 2], 1));

  parts = Pseq([part1, part2], inf);

  instrument1 = Pbindf(parts,
    \instrument, \a,
    \freq_attack, 0,
    \freq_decay, 0.05,
    \decay, 0.05,
    \attack, 0,
    \freq_bias, midicps(Pkey(\midinote) + 15),
  ); 

  instrument2 = Pbindf(parts,
    \instrument, \b,
    \midinote, Pkey(\midinote) - 36,
    \attack, 0,
    \decay, 0.02,
    \sustain, 0.05,
    \amp, 0.5,
  ); 

  kick = Pbindf(parts,
    \instrument, \kick,
    \sustain_level, 1,
    \midinote, Pkey(\midinote) - 48,
    \freq_peak, midicps(Pkey(\midinote) + 24),
    \amp, 1,
  ); 

  // Ppar([instrument1, instrument2]).play();
  Ptpar([
    0, instrument1,
    // 0, instrument2,
    0, kick
  ]).play();
)
