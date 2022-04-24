// package com.github.caay2000.ttk.context.vehicle.application.handler.vehicle
//
// import arrow.core.Option
// import com.github.caay2000.ttk.context.shared.domain.toDomainId
// import com.github.caay2000.ttk.context.vehicle.application.vehicle.create.CreateVehicleCommand
// import com.github.caay2000.ttk.context.vehicle.application.vehicle.create.CreateVehicleCommandHandler
// import com.github.caay2000.ttk.context.vehicle.domain.repository.WorldRepository
// import org.junit.jupiter.api.Test
// import org.junit.jupiter.api.assertThrows
// import org.mockito.kotlin.mock
// import org.mockito.kotlin.whenever
// import java.util.UUID
//
// internal class CreateVehicleCommandHandlerTest {
//
//    private val worldRepository: WorldRepository = mock()
//
//    private val sut = CreateVehicleCommandHandler(mock(), worldRepository, mock())
//
//    @Test
//    fun `aa`() {
//
//        val worldId = UUID.randomUUID()
//        val command = CreateVehicleCommand(worldId, UUID.randomUUID(), "TRUCK", "FACTORY")
//
//        whenever(worldRepository.get(worldId.toDomainId())).thenReturn(Option.fromNullable(null))
//
//        assertThrows<RuntimeException> {
//            sut.invoke(command)
//        }
//    }
// }
