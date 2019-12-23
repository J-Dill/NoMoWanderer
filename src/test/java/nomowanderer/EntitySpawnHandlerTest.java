package nomowanderer;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.junit.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class EntitySpawnHandlerTest {

    @Test
    public void entityShouldSpawn() {
        LivingSpawnEvent.SpecialSpawn event = mock(LivingSpawnEvent.SpecialSpawn.class);
        Entity entity = mock(Entity.class);
        when(event.getEntity()).thenReturn(entity);
        verifyNoMoreInteractions(entity);
        EntitySpawnHandler.maybeBlockTraderSpawn(event);
    }

}
