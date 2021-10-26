package net.oskarstrom.dashloader.def.font;

import net.oskarstrom.dashloader.core.data.Pointer2PointerMap;
import net.oskarstrom.dashloader.def.api.DashDataType;
import net.oskarstrom.dashloader.def.api.DashObject;
import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import net.minecraft.client.font.UnicodeTextureFont;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;
import net.oskarstrom.dashloader.core.registry.DashRegistry;
import net.oskarstrom.dashloader.def.mixin.accessor.UnicodeTextureFontAccessor;
import net.oskarstrom.dashloader.def.util.UnsafeHelper;

import java.util.HashMap;
import java.util.Map;

@DashObject(value = UnicodeTextureFont.class)
public class DashUnicodeFont implements DashFont {
	@Serialize(order = 0)
	public final Pointer2PointerMap images;

	@Serialize(order = 1)
	public final byte[] sizes;

	@Serialize(order = 2)
	public final String template;


	public DashUnicodeFont(@Deserialize("images") Pointer2PointerMap images,
						   @Deserialize("sizes") byte[] sizes,
						   @Deserialize("template") String template) {
		this.images = images;
		this.sizes = sizes;
		this.template = template;

	}

	public DashUnicodeFont(UnicodeTextureFont rawFont, DashRegistry registry) {
		images = new Pointer2PointerMap();
		UnicodeTextureFontAccessor font = ((UnicodeTextureFontAccessor) rawFont);
		font.getImages().forEach((identifier, nativeImage) -> images.add(Pointer2PointerMap.Entry.of(registry.add(identifier), registry.add(nativeImage))));
		this.sizes = font.getSizes();
		this.template = font.getTemplate();
	}


	public UnicodeTextureFont toUndash(DashExportHandler exportHandler) {
		Map<Identifier, NativeImage> out = new HashMap<>(images.size());
		images.forEach((entry) -> out.put(registry.get(entry.key), registry.get(entry.value)));
		UnicodeTextureFont font = UnsafeHelper.allocateInstance(UnicodeTextureFont.class);
		UnicodeTextureFontAccessor accessor = ((UnicodeTextureFontAccessor) font);
		accessor.setSizes(sizes);
		accessor.setImages(out);
		accessor.setTemplate(template);
		return font;
	}
}