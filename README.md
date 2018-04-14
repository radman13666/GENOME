# GENOME
This is a command-line Java metronome app that features speed, gap and timed clicks. It was made for musicians (drummers especially) 
who have an affinity for the command-line environment and love to practice in an organised way (like me B-D). 

It basically allows a musician to specify a (metronome-based) practice routine as command-line arguments and then it plays 
that routine in order as specified. 

This application is supposed to fulfil the following requirements:

1. play a click track for a specified number of minutes, to train endurance.
2. play a click track for a certain amount of time, and then stay quiet for certain amount of time periodically, to train groove and time.
3. play a click track which increases in tempo after a certain amount of time, to train control at various tempos.
4. play a series of these metronomes, and leave some time between each for practice breaks.

The way I designed this app was as follows:

### TIMED METRONOME
This feature takes in the following parameters: ```tempo```, ```measure``` and ```duration```. 
This means that a musician would play to this metronome for ```duration``` minutes. That's it.

### SPEED METRONOME
This feature takes in the following parameters: ```start tempo```, ```end tempo```, ```tempo length```, ```tempo increment``` and ```measure```.
This means that a musician would play to this metronome starting from ```start tempo``` bpm, and it gradually increases the tempo by ```tempo increment``` bpms every after ```tempo length``` measures, until it reaches ```end tempo``` bpm (or greater).

### GAP METRONOME
This feature takes in the following parameters: ```loud measures```, ```silent measures```, ```gap length increment```, ```gap repetitions```, ```tempo```, ```measure``` and ```duration```.
This means that a musician would play to this metronome with ```silent measures``` increasing by ```gap lenght increment``` measures every after ```gap repetitions``` measures. All this happens for ```duration``` minutes.


### FUTURE PROSPECTS
This app had some other features it was supposed to fulfil (in GUI form) such as:
1. Progress tracker
2. Routine Generator
3. Click track maker (for out-of-app use as .mp3 files or something)
4. Play Along/Loop repository access
5. "Drum Toasts" (short, informative and/or entertaining messages contributed by the drummer/music community worldwide)

This application was to be a GUI application from the start but developing it needed time to think. So, I launched this version instead (I really needed this metronome for my kind of practice. No one out there had it for free, so I built my own B-D). <a href="https://drive.google.com/open?id=1dS68AuBpiBZpffO775SDYh1ULcljrcBY">Get it now<a/>.
I want this metronome to revolutionize musician's practice everywhere and become the ultimate tool for musicians to get better at what they do.
I realise that there's still alot to be desired in this application, and any contributions would greatly be appreciated.
