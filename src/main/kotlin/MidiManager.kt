import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import tornadofx.Dimension
import javax.sound.midi.MidiSystem
import javax.sound.midi.MidiUnavailableException
import javax.sound.midi.Soundbank
import javax.sound.midi.Synthesizer
import javax.sound.sampled.spi.AudioFileReader

object MidiManager {

    private lateinit var midiSynth: Synthesizer

    enum class State { PLAY, STOP, PAUSE }

    var state: State = State.STOP

    fun play() {
        state = State.PLAY

        try {

            val worker: Scheduler.Worker = Schedulers.newThread().createWorker()
            worker.schedule {
                midiSynth = MidiSystem.getSynthesizer().apply { open() }
                (0..128).forEach {
                    println("it: $it")


                    PlayPhrase(it)
                }

                /*Observable.fromCallable {
                    PlayPhrase(it)
                }
                        //.subscribeOn(Schedulers.single())
                        .takeWhile { state == State.PLAY }
                        .subscribe()
*/
            }

        } catch (ex: MidiUnavailableException) {
            println("Exception: ${ex.message}")
        } finally {
            stop()
        }

        //val audioFileReader = AudioFileReader()
    }

    fun stop() {
        state = State.STOP
        midiSynth.close()
    }

    private fun PlayPhrase(instrumentNumber: Int) {
        println("Thread: ${Thread.currentThread().name}")

        playNote(
                instrumentNumber,
                0,
                noteNumber = 60,
                noteVelocity = 100,
                holdNoteTime = 800
        )

        playNote(
                instrumentNumber,
                0,
                noteNumber = 69,
                noteVelocity = 50,
                holdNoteTime = 1333
        )

        playNote(
                instrumentNumber,
                0,
                noteNumber = 43,
                noteVelocity = 89,
                holdNoteTime = 970
        )

        playNote(
                instrumentNumber,
                0,
                noteNumber = 37,
                noteVelocity = 134,
                holdNoteTime = 2733
        )
    }

    fun playNote(instrumentNumber: Int, midiChannelNumber: Int = 0, noteNumber: Int, noteVelocity: Int, holdNoteTime: Long) {
        val instrumentsArray = midiSynth.defaultSoundbank.instruments
        val instrument = instrumentsArray[instrumentNumber]
        val midiChannels = midiSynth.channels
        midiSynth.loadInstrument(instrument)
        midiChannels[midiChannelNumber].programChange(instrument.patch.program)
        midiChannels[midiChannelNumber].noteOn(noteNumber, noteVelocity)
        println("now playing instrument: ${instrument.name}")
        try {
            Thread.sleep(holdNoteTime)
        } catch (ex: MidiUnavailableException) {
            println("Exception: ${ex.message}")
        }
        midiChannels[0].noteOff(noteNumber)

/*    val soundbank: Soundbank = SoundFon
    soundbank.*/
    }

}

