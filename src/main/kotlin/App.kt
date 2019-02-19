import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.stage.Stage
import tornadofx.action

fun main(args: Array<String>) {
    launch(HelloWorld::class.java)
}

class HelloWorld : Application() {
    override fun start(primaryStage: Stage?) {


        val layout = VBox().apply {

            val introLabel = Label("Midi Test")

            val resultLabel = Label("")

            val playButton = Button("Play").apply {
                action {
                    resultLabel.setText("Playing..")
                    MidiManager.play()
                }
            }

            val stopButton = Button("Stop").apply {
                action {
                    resultLabel.setText("..Now stopped.")
                    MidiManager.stop()
                }
            }

            children.add(introLabel)
            children.add(playButton)
            children.add(stopButton)
            children.add(resultLabel)
        }

        primaryStage?.run {
            scene = Scene(layout, 400.0, 300.0)
            show()
        }
    }
}