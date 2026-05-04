# Cuentas Claras

App Android de finanzas personales para controlar gastos, ingresos y presupuestos mensuales.

## Stack

| Categoria | Tecnologia |
|-----------|-----------|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material Design 3 |
| Arquitectura | Clean Architecture + MVVM |
| DI | Hilt |
| Base de datos | Room |
| Async | Coroutines + Flow |
| Navegacion | Navigation Compose |
| Preferencias | DataStore |
| Graficas | Vico |
| Testing | JUnit + MockK + Turbine |

## Arquitectura modular

```
CuentasClaras/
├── app/                  -> Entry point, navegacion, splash screen
├── core/                 -> Modelos, Room DB, repositorios, use cases, DI
├── core-ui/              -> Tema, componentes reutilizables (Atomic Design)
├── feature-home/         -> Pantalla de inicio con balance y transacciones
├── feature-add/          -> Agregar ingreso o gasto
├── feature-charts/       -> Graficas por categoria y por mes
└── feature-budget/       -> Presupuestos por categoria
```

### Dependencias entre modulos

```
app
 ├── feature-home
 ├── feature-add
 ├── feature-charts
 └── feature-budget
      ├── core
      └── core-ui
```

> Cada feature depende de `core` y `core-ui`.

## Patrones y principios

- **Clean Architecture**: separacion en capas data, domain y presentation
- **MVVM**: cada feature tiene su ViewModel con UiState sealed class
- **Atomic Design**: componentes UI organizados en atoms, molecules y organisms
- **Single Source of Truth**: Room como fuente unica de datos
- **UiState Pattern**: manejo de estados Loading, Success y Error

## Funcionalidades

- Registro de ingresos y gastos por categoria
- Balance general con resumen de ingresos vs gastos
- Historial de transacciones con filtros por mes
- Graficas de gastos por categoria y por periodo
- Presupuestos mensuales con alertas de limite
- Multiples cuentas (efectivo, banco, tarjeta)

## Requisitos

- Android 8.0+ (API 26)
- Android Studio Meerkat o superior

## Autor

**Jorge Alejandro Jaramillo** - [@JorgeAlejoJara](https://github.com/JorgeAlejoJara)
