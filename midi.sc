~kickFactory.();

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

// when done:
q.value;
