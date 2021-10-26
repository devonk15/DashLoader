package net.oskarstrom.dashloader.def.blockstate.property;

import dev.quantumfusion.hyphen.scan.annotations.DataNullable;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;
import net.oskarstrom.dashloader.core.registry.DashExportHandler;
import net.oskarstrom.dashloader.def.api.DashObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@DashObject(DirectionProperty.class)
public record DashDirectionProperty(String name, @DataNullable Direction[] directions) implements DashProperty {

	public static DashDirectionProperty create(DirectionProperty property) {
		var name = property.getName();
		Direction[] directions;

		final Collection<Direction> values = property.getValues();
		final int size = values.size();

		if (size == 6) {
			directions = null;
		} else {
			List<Direction> directionsOut = new ArrayList<>(values);
			directions = new Direction[directionsOut.size()];
			for (int i = 0; i < directionsOut.size(); i++) {
				directions[i] = directionsOut.get(i);
			}
		}

		return new DashDirectionProperty(name, directions);
	}

	@Override
	public DirectionProperty toUndash(DashExportHandler exportHandler) {
		return DirectionProperty.of(name, directions == null ? Direction.values() : directions);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DashDirectionProperty that = (DashDirectionProperty) o;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}