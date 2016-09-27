~fmFactory.(\fm, 3, \sine);

(
  ~test = 0.15;
  a = Pbind(
    \instrument, \fm,
    \midinote, Pstutter(2, 56 + Pseq([Pn(4, 3), Pn(0, 6), Pn(2, 3)], inf) + Pseq([Pn([0, 3, 7], 3), Pn([0, 4, 7], 9)], inf)),
    \tempo, 3,
    \dur, 0.15,
    \delta, Pstutter(2, 0.5 * Pseq([1, 0.5, 0.25, Pn(0.75, 3)], inf)),
    \attack, 0.05,
    \decay, 0.1,
    \fm_ratio, 2,
    \fm_beta, ~test,
  );

  b = Pbind(
    \instrument, \fm,
    \midinote, 40,
    \tempo, 3,
    \dur, 0.15,
    \delta, Pn(1, inf),
    \amp, 0.2,
    \fm_ratio, 3.4,
    \fm_beta, 2.1,
  );

  Ptpar([0, a, 0, b]).play();
)
