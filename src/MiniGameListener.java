public interface MiniGameListener {
    void onReactionMiniGameComplete(boolean passed);
    void onTimeBasedMiniGameComplete(boolean passed);
    void onPauseMenu();
}
