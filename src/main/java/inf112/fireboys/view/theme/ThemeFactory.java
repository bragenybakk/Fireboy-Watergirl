package inf112.fireboys.view.theme;

/**
 * Object factory for Theme implementations. Functional interface — use
 * constructor references: {@code CastleTheme::new} or {@code NullTheme::new}.
 */
@FunctionalInterface
public interface ThemeFactory {
    Theme create();
}
