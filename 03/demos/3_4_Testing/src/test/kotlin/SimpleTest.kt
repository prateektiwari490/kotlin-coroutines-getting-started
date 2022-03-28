import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class SimpleTest {

    // 3. SHow test does not work
    @Test
    fun firstTest() {
//        doWork()
        assertEquals(2, 1 + 1)
    }

    @Test
    fun secondTest() = runBlocking {
        doWork()
        assertEquals(2, 1 + 1)
    }
}