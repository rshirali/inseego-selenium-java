# Inseego Playwright TypeScript Framework

A clean, professional-grade Playwright + TypeScript test framework built to validate public routes and UI flows for **Inseego.com**.

## Prerequisites
- Node.js 20+
- npm 9+
- Playwright 1.56+

Install dependencies:
```bash
npm ci
```

## Folder Structure
```
├── README.md
├── package.json
├── playwright.config.ts
├── src/
│   └── pages/
│       └── HomePage.ts
├── tests/
│   └── inseego.headerFooter.spec.ts
├── utils/
│   ├── ui.ts
│   └── printResolvedConfig.ts
```

## Run Tests
### Headed Chrome
```bash
BASE_URL=https://inseego.com npm run test:headed:chrome
```

### Headed Firefox
```bash
BASE_URL=https://inseego.com npm run test:headed:firefox
```

### Headless (default)
```bash
npm run test
```

### Print Effective Config
```bash
npm run pw:print
```

## Reports
After a run:
```bash
npx playwright show-report
```
Screenshots (if enabled with `SCREENSHOT=true`) are available in `target/test-output/artifacts`.

## Git Hygiene
When contributing:
- Use clear commit prefixes: `feat:`, `fix:`, `refactor:`, `docs:`
- Keep commits atomic and testable
- Avoid committing reports, node_modules, or local debug files

## Author
Rajeev Shirali — QA Automation Architect
