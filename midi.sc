s.boot;
s.quit;

// for polyphonic synth

(
  var notes, velocities, on, off, aftertouch, polytouch, pitchbend;

  MIDIClient.init;
  MIDIIn.connectAll;

  notes = Array.newClear(128);    // array has one slot per possible MIDI note
  velocities = Array.newClear(128);    // array has one slot per possible MIDI note

  on = MIDIFunc.noteOn({ |veloc, num, chan, src|
    notes[num] = Synth(\sine, [
      \freq, num.midicps,
      \amp, veloc * 0.00315,
      \sustain_level, 0.5,
    ]);
    velocities[num] = veloc;
  });

  aftertouch = MIDIFunc.touch({ |val, chan, src|
    var aftertouchScale = 1 + (val / 32);
    notes.do({ | synth, index |
      if ( (synth != nil) && (velocities[index] != nil),
        {
          synth.set(\amp, velocities[index] * 0.00315 * aftertouchScale)
        }
      );
    });
  });

  pitchbend = MIDIFunc.bend({ |val, chan, src|

    var normalVal = ((val + 1) / 128) - 1,
        pitchbendScale = 1 + ((normalVal - 63) / (2 * 128));
    notes.do({ | synth, index |
      if ( (synth != nil), { synth.set(\freq, index.midicps * pitchbendScale) });
    });
  });

  off = MIDIFunc.noteOff({ |veloc, num, chan, src|
    notes[num].release;
    notes[num] = nil;
    velocities[num] = nil;
  });

  q = { on.free; off.free; };
)

(16384 / (4 * pow(128, 2))).postln;

h = Array.newClear(4);
if (h[1] == nil, "4", "5");

if (true && false, "4", "5");

// when done:
q.value;

// TODO for monophonic synth
(
  var notes, on, off;

  MIDIClient.init;
  MIDIIn.connectAll;

  notes = Array.newClear(128);    // array has one slot per possible MIDI note

  on = MIDIFunc.noteOn({ |veloc, num, chan, src|
    notes[num] = Synth(\kick, [
      \freq, num.midicps,
      \amp, veloc * 0.00315,
      \freq_peak, (num + 24).midicps,
      \sustain_level, 1,
    ]);
  });

  off = MIDIFunc.noteOff({ |veloc, num, chan, src|
    notes[num].release;
  });

  q = { on.free; off.free; };
)
