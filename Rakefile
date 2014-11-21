KARMA = "node_modules/karma/bin/karma"
KARMA_CONFIG = "src/test/js/config/karma.unit.js"

namespace :npm do
  task :install do
    sh "npm install"
  end
end

namespace :test do
  namespace :karma do
    desc "Start the Karma test runner"
    task :start => 'npm:install' do
      sh "#{KARMA} start #{KARMA_CONFIG}"
    end

    desc "Run the Karma test runner once"
    task :run => 'npm:install' do
      sh "#{KARMA} start #{KARMA_CONFIG} --single-run"
    end

    desc "Start the CI Runner"
    task :ci  => 'npm:install' do
      sh "#{KARMA} start #{KARMA_CONFIG} --browsers PhantomJS --single-run --reporters progress,junit"
    end
  end

  task :mvn do
    sh "mvn test"
  end
end

task :ci => ["test:mvn", "test:karma:ci"]