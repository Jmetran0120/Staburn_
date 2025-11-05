import 'zone.js';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';


platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => {
    console.error('Error bootstrapping Angular application:', err);
    // Display error on page if bootstrap fails
    document.body.innerHTML = '<div style="padding: 20px; color: red;"><h1>Angular Bootstrap Error</h1><pre>' + JSON.stringify(err, null, 2) + '</pre></div>';
  });
