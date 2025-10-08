package fuzs.linkedchests.client.renderer.blockentity;

import java.util.function.Function;

/**
 * @see net.minecraft.client.renderer.entity.ArmorModelSet
 */
public record LinkedChestModelSet<T>(T chest, T lock, T leftButton, T middleButton, T rightButton) {

    public T getButton(int index) {
        return switch (index) {
            case 0 -> this.leftButton;
            case 1 -> this.middleButton;
            case 2 -> this.rightButton;
            default -> throw new IllegalStateException("No model for index: " + index);
        };
    }

    public <U> LinkedChestModelSet<U> map(Function<? super T, ? extends U> mapper) {
        return new LinkedChestModelSet<>(mapper.apply(this.chest),
                mapper.apply(this.lock),
                mapper.apply(this.leftButton),
                mapper.apply(this.middleButton),
                mapper.apply(this.rightButton));
    }
}
