package fambot;

import rlbot.Bot;
import rlbot.manager.BotManager;
import rlbot.pyinterop.SocketServer;

public class FamBotPythonInterface extends SocketServer {

    public FamBotPythonInterface(int port, BotManager botManager) {
        super(port, botManager);
    }

    @Override
    protected Bot initBot(int index, String botType, int team) {
        return new FamBot07(index);
    }
}
